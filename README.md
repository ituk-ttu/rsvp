## Event endpoints

|   |   |
|---|---|
|**Description**| Get all events in database 
|**URL**        | `/events`, `/events/`, `/events/all`
|**Method**     | `GET`
|**URL Params** | None
|**Data Params** | None 
|**Success Response** | Code: 200 Content: All found events
|**Error Response** | Code: 500, Content: None |

|   |   |
|---|---|
|**Description**| Create event
|**URL**        | `/events/`, `/events/create`
|**Method**     | `POST`
|**URL Params** | None
|**Data Params** | None
|**Success Response** | Code: 201, Content: None
|**Error Response** | Code: 400, Content: JSON list of errors in english<br> Code: 500, Content: None

|   |   |
|---|---|
|**Description**| Delete event from database by id
|**URL**        | `/events/{id}`, `/events/delete/{id}`
|**Method**     | `DELETE`
|**URL Params** | Id of event
|**Data Params** | None
|**Success Response** | Code: 200, Content: None
|**Error Response** | Code: 404, Content: `{error: "Event not found"}` <br> Code: 500, Content: None

