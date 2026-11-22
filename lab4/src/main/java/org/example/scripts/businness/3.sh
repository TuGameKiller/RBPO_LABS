#!/bin/bash
curl -i -X POST http://localhost:8080/api/loans/4/extend \
  -H "Content-Type: application/json" \
  -d '{"date":"2026-12-05"}'