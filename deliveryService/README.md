# Data model

## Box
```json
{
  "id" : "...",
  "name" : "...",
  "address" : "...",
  "rasPiId" : "...",
  "deliveries" : [{"...":  "...."}, {"...":  "..."}]
}
```

## Delivery
```json
{
  "id" : "...",
  "status" : "...",
  "customer" : "...",
  "deliverer" : "..."
}
```
- Hidden: box where it is delivered to (TODO: find a way to include information about box without stackoverflow)
- possible status: ORDERED, PICKED_UP, IN_TARGET_BOX, DELIVERED

## Additional Notes
- A box only contains deliveries that are not yet delivered
- A delivery retains a reference to the box that it is/has been delivered to, even after successful delivery

# API Endpoints

| endpoint                | Methods             | body                   | output                     | comment                                         |
|-------------------------|---------------------|------------------------|----------------------------|-------------------------------------------------|
| /box                    | GET (DISPATCHER)    |                        | list of all boxes          |                                                 |
| /box/{boxId}            | GET                 |                        | information about a box    |                                                 |
| /box/{boxId}/deliveries | GET                 |                        | active deliveries of a box |                                                 |
| /box/create             | POST (DISPATCHER)   | name, address, rasPiId | new box (created)          |                                                 |
| /box/{boxId}            | PUT (DISPATCHER)    | name, address, rasPiId | box (updated)              | only possible if box has no deliveries assigned |
| /box/{boxId}            | DELETE (DISPATCHER) |                        |                            | only possible if box has no deliveries assigned |


| endpoint                              | Methods                   | body                | output                                                                              | comment                                                                        |
|---------------------------------------|---------------------------|---------------------|-------------------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| /delivery                             | GET (DELIVERER, CUSTOMER) |                     | list of all deliveries concerning this aseUser                                      |                                                                                |
| /delivery/all                         | GET (DISPATCHER)          |                     | list of all deliveries                                                              |                                                                                |
| /delivery/{deliveryId}                | GET                       |                     | information about a delivery                                                        | only authorized if this delivery concerns the aseUser or aseUser is dispatcher |
| /delivery/{deliveryId}/box            | GET                       |                     | information about the box that is used to deliver this delivery                     | only authorized if this delivery concerns the aseUser or aseUser is dispatcher |
| /delivery/{boxId}                     | POST (DISPATCHER)         | customer, deliverer | create a new delivery                                                               | initial status is ORDERED                                                      |
| /delivery/{deliveryId}                | PUT (DISPATCHER)          | customer, deliverer | update a delivery                                                                   | status can be updated via different endpoint                                   |
| /delivery/{deliveryId}/assign/{boxId} | PUT (DISPATCHER)          |                     | reassign a delivery                                                                 | only possible if delivery has status ORDERED                                   |
| /delivery/{deliveryId}/pickup         | PUT (DELIVERER)           |                     | change the status of a delivery to PICKED_UP                                        |                                                                                |
| /delivery/place                       | PUT (BOX)                 |                     | change the status of all deliveries in a box with status PICKED_UP to IN_TARGET_BOX |                                                                                |
| /delivery/retrieve                    | PUT (BOX)                 |                     | change the status of all deliveries in a box with status IN_TARGET_BOX to DELIVERED |                                                                                |
| /delivery/{deliveryId}                | DELETE (DISPATCHER)       |                     |                                                                                     | only possible if status is ORDERED or DELIVERED                                | 