#!/bin/bash
curl -i -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"igor_reader","password":"secret123","role":"READER","status":"ENABLED"}'