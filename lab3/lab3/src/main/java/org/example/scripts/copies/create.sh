#!/bin/bash
curl -X POST http://localhost:8080/api/copies \
  -H "Content-Type: application/json" \
  -d '{"bookEditionId": 13, "inventoryNumber": "INV-004", "status": "AVAILABLE"}'
