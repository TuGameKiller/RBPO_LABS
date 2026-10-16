#!/bin/bash
curl -X POST http://localhost:8080/api/book-editions \
	-H "Content-Type: application/json" \
	-d '{"title": "Капитанская дочка", "author": "Пушкин А.С.", "isbn": "978-5-12-345678-9"}'
