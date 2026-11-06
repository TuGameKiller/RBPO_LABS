#!/bin/bash
curl -X POST http://localhost:8080/api/copies \
  -u admin:admin12345 \
  -H "Content-Type: application/json" \
  -d '{"bookEditionId": 1, "inventoryNumber": "INV-004", "status": "AVAILABLE"}'
