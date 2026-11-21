#!/bin/bash
curl -X PATCH http://localhost:8080/api/copies/1 \
  -H "Content-Type: application/json" \
  -d '{"inventoryNumber":"INV-002"}'