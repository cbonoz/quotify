package main

import (
	"net/http"
	"strconv"

	_ "github.com/go-sql-driver/mysql"

	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/sqlite"
	"github.com/labstack/echo"
	"github.com/labstack/echo/middleware"
)

type (
	user struct {
		gorm.Model
		Name     string `json:"name"`
		Password string `json:"password"`
	}
)

type (
	// gorm.Model -> Add fields `ID`, `CreatedAt`, `UpdatedAt`, `DeletedAt`
	question struct {
		gorm.Model
		Author string `json:"author";sql:"size:255;index"`
		Body   string `json:"body"`
		Answer string `json:"answer"`
		Rating int    `json:"rating"`
	}

	answer struct {
		User       string `json:"user";sql:"size:255;index"`
		QuestionID uint   `json:"question_id"`
		Correct    bool   `json:"correct"`
	}
)

var (
	users = map[int]*user{}
	// Map from question id to Question.
	// questions = map[int]*question{}
	seq = 1
)

var db *gorm.DB

//----------
// Handlers
//----------

func createUser(c echo.Context) error {
	var u *user
	if err := c.Bind(u); err != nil {
		return err
	}
	id, _ := strconv.Atoi(c.Param("id"))
	users[id] = u
	seq++
	return c.JSON(http.StatusCreated, u)
}

func getUser(c echo.Context) error {
	id, _ := strconv.Atoi(c.Param("id"))
	return c.JSON(http.StatusOK, users[id])
}

func updateUser(c echo.Context) error {
	u := new(user)
	if err := c.Bind(u); err != nil {
		return err
	}
	id, _ := strconv.Atoi(c.Param("id"))
	users[id].Name = u.Name
	return c.JSON(http.StatusOK, users[id])
}

func deleteUser(c echo.Context) error {
	id, _ := strconv.Atoi(c.Param("id"))
	delete(users, id)
	return c.NoContent(http.StatusNoContent)
}

// Question handling.
func createQuestion(c echo.Context) error {
	q := new(question)
	err := c.Bind(q)
	if err != nil {
		return err
	}
	db.Save(&q)
	// b, err := json.Marshal(q)
	// if err != nil {
	// 	fmt.Println(err)
	// }
	return c.JSON(http.StatusCreated, &q)
}

func helloThere(c echo.Context) error {
	q := new(question)
	err := c.Bind(q)
	if err != nil {
		return err
	}
	return c.JSON(http.StatusOK, "hello there: "+q.Author)
}

func getQuestionById(c echo.Context) error {
	id := c.Param("id")
	var q question

	if db.First(&q, "ID = ?", id).Error != nil {
		return c.JSON(http.StatusOK, "None")
	}
	return c.JSON(http.StatusOK, q)
}

func getQuestionsByAuthor(c echo.Context) error {
	author := c.Param("author")
	var qs []question
	// db.Where("author = ?", author).Find(&qs)
	db.Raw("select * from questions where author = ?", author).Scan(&qs)
	return c.JSON(http.StatusOK, qs)
}

func getQuestions(c echo.Context) error {
	var qs []question
	// db.Find(&qs)
	db.Raw("select * from questions").Scan(&qs)
	return c.JSON(http.StatusOK, qs)
}

func updateQuestion(c echo.Context) error {
	u := new(question)
	if err := c.Bind(u); err != nil {
		return err
	}
	// id, _ := strconv.Atoi(c.Param("id"))
	db.Save(&u)
	return c.JSON(http.StatusOK, u)
}

func deleteQuestion(c echo.Context) error {
	id, _ := strconv.Atoi(c.Param("id"))
	db.Delete("ID = ?", id)
	return c.NoContent(http.StatusNoContent)
}

// Examples:
// curl -H "Content-Type: application/json" -X POST -d '{"author": "chris"}' localhost:9001/question/add
// curl -i localhost:9001/question/list
// db, err = gorm.Open("postgres", "user=cbono password=admin host=localhost port=1324 dbname=quotify sslmode=disable")
// db, err = gorm.Open("sqlite3", databaseURL)
func main() {
	var err error
	e := echo.New()

	// default port is 3306 for mysql
	qqq := ":interview1"
	databaseURL := "cbono" + qqq + "@tcp(localhost:3306)/quotify?charset=utf8&parseTime=True&loc=Local"
	db, err = gorm.Open("mysql", databaseURL)

	if err != nil {
		panic(err)
	}
	defer db.Close()
	// Get database connection handle [*sql.DB](http://golang.org/pkg/database/sql/#DB)
	db.DB()

	// Then you could invoke `*sql.DB`'s functions with it
	db.DB().Ping()
	db.DB().SetMaxIdleConns(10)
	db.DB().SetMaxOpenConns(100)

	// db.DropTableIfExists(&question{})
	db.AutoMigrate(&question{})
	// db.AutoMigrate(&answer)

	// Middleware
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())

	// Routes
	e.POST("/users", createUser)
	e.GET("/users/:id", getUser)
	e.PUT("/users/:id", updateUser)
	e.DELETE("/users/:id", deleteUser)

	e.POST("/question/add", createQuestion)
	e.GET("/question/list", getQuestions)
	e.GET("/question/id/:id", getQuestionById)
	e.GET("/question/author/:author", getQuestionsByAuthor)
	e.PUT("/questions/:id", updateQuestion)
	e.DELETE("/questions/delete/:id", deleteQuestion)

	e.POST("/hello", helloThere)

	// Start server
	if err = e.Start(":9001"); err != nil {
		e.Logger.Fatal(err.Error())
	}
}
