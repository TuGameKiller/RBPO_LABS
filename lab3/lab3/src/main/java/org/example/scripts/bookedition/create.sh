#!/bin/bash
curl -X POST http://localhost:8080/api/book-editions \
	-H "Content-Type: application/json" \
	-d '{"title": "ОНЕГИН", "author": "Пушкин А.С.", "isbn": "978-4-12-345673-9"}'
