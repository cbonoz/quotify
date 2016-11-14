package main

import (
	"net/http"
	"strconv"

	"github.com/jinzhu/gorm"
	"github.com/labstack/echo"
	"github.com/labstack/echo/middleware"
)

type (
	user struct {
		ID   int    `json:"id"`
		Name string `json:"name"`
	}
)

type (
	question struct {
		gorm.Model
		ID     int    `json:"id"`
		Author string `json:"author"`
		Body   string `json:"body"`
		Answer string `json:"answer"`
		Rating int    `json:"rating"`
	}
)

var (
	users = map[int]*user{}
	// Map from question id to Question.
	questions = map[int]*question{}
	seq       = 1
	db        = gorm.DB
)

//----------
// Handlers
//----------

func createUser(c echo.Context) error {
	u := &user{
		ID: seq,
	}
	if err := c.Bind(u); err != nil {
		return err
	}
	users[u.ID] = u
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
	u := &question{
		ID: seq,
	}
	if err := c.Bind(u); err != nil {
		return err
	}
	questions[u.ID] = u
	seq++
	return c.JSON(http.StatusCreated, u)
}

func getQuestion(c echo.Context) error {
	id, _ := strconv.Atoi(c.Param("id"))
	return c.JSON(http.StatusOK, questions[id])
}

func updateQuestion(c echo.Context) error {
	u := new(question)
	if err := c.Bind(u); err != nil {
		return err
	}
	id, _ := strconv.Atoi(c.Param("id"))

	questions[id] = u
	return c.JSON(http.StatusOK, questions[id])
}

func deleteQuestion(c echo.Context) error {
	id, _ := strconv.Atoi(c.Param("id"))
	delete(questions, id)
	return c.NoContent(http.StatusNoContent)
}

// Examples:
// curl -H "Content-Type: application/json" -X T -d '{"id": 123, "author": "chris"}' localhost:1323/questions
func main() {
	e := echo.New()

	var err error
	db, err = gorm.Open("postgres", "user=cbono password=123 dbname=quotify sslmode=disable")
	if err != nil {
		panic(err)
	}
	defer db.Close()

	db.AutoMigrate(&question{})

	// Middleware
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())

	// Routes
	e.POST("/users", createUser)
	e.GET("/users/:id", getUser)
	e.PUT("/users/:id", updateUser)
	e.DELETE("/users/:id", deleteUser)

	e.POST("/questions", createQuestion)
	e.GET("/questions/:id", getQuestion)
	e.PUT("/questions/:id", updateQuestion)
	e.DELETE("/questions/:id", deleteQuestion)

	// Start server
	if err := e.Start(":1323"); err != nil {
		e.Logger.Fatal(err.Error())
	}
}
