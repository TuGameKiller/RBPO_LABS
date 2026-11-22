#!/bin/bash
curl -i -X POST http://localhost:8080/api/copies \
  -H "Content-Type: application/json" \
  -d '{"bookEditionId":1,"inventoryNumber":"INV-001","status":"AVAILABLE"}'