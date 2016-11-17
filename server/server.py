# """Quotify Flask Server"""

# from flask import Flask
# from flask import abort
# from flask import jsonify
# from flask_sqlalchemy import SQLAlchemy
# from models import Question
# from models import User

# app = Flask(__name__)
# app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:////tmp/test.db'
# db = SQLAlchemy(app)


# class Question(db.Model):
#     id = db.Column(db.Integer, primary_key=True)
#     author = db.Column(db.String(80), unique=True)
#     body = db.Column(db.String(80))
#     answer = db.Column(db.String(80))
#     rating = db.Column(db.Integer)

#     def __init__(self, author, body, answer, rating):
#         self.author = author
#         self.body = body
#         self.answer = answer
#         self.rating = rating
    
#     def __repr__(self):
#         return '<Question %r>' % self.body

# class User(db.Model):
#     id = db.Column(db.Integer, primary_key=True)
#     username = db.Column(db.String(80), unique=True)
#     email = db.Column(db.String(120), unique=True)

#     def __init__(self, username, email):
#         self.username = username
#         self.email = email

#     def __repr__(self):
#         return '<User %r>' % self.username

# def create_sample_question():
#     q1 = Question("chris", "How many questions are here?", "about 5", 0)
#     db.session.add(q1)
#     db.session.commit()

# def main():
#     db.create_all()
#     create_sample_question()
#     app.run(debug=True)

# @app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['GET'])
# def get_task(task_id):
#     task = [task for task in tasks if task['id'] == task_id]
#     if len(task) == 0:
#         abort(404)
#     return jsonify({'task': task[0]})

# @app.route('/questions/<int:id', methods=['GET'])
# def get_question(question_id):

# if __name__ == "__main__":
#     main()
    