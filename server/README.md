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
|**Notes** | 


|   |   |
|---|---|
|**Description**| Create event
|**URL**        | `/events/`, `/events/create`
|**Method**     | `POST`
|**URL Params** | None
|**Data Params** | ```{"creatorId": String, "eventName": String, "eventTime": Long, "eventPlace": String, "info": String, "inviteExpire": Long } ```
|**Success Response** | Code: 201, Content: None
|**Error Response** | Code: 400, Content: JSON list of errors in english <br> Code: 500, Content: None
|**Notes** | `eventTime` and `inviteExpire` use seconds from epoch format

|   |   |
|---|---|
|**Description**| Edit event
|**URL**        | `/events/{id}`, `/events/edit/{id}`
|**Method**     | `PUT`
|**URL Params** | Id of event
|**Data Params** | ```{"creatorId": String, "eventName": String, "eventTime": Long, "eventPlace": String, "info": String, "inviteExpire": Long } ```
|**Success Response** | Code: 200, Content: `{"eventId": Long}`
|**Error Response** | Code: 400, Content: JSON list of errors in english <br> Code: 404, Content: `{error: "Event not found"}` <br> Code: 500, Content: None
|**Notes** | `eventTime` and `inviteExpire` use seconds from epoch format

|   |   |
|---|---|
|**Description**| Delete event from database by id
|**URL**        | `/events/{id}`, `/events/delete/{id}`
|**Method**     | `DELETE`
|**URL Params** | Id of event
|**Data Params** | None
|**Success Response** | Code: 200, Content: None
|**Error Response** | Code: 404, Content: `{error: "Event not found"}` <br> Code: 500, Content: None
|**Notes** | 
---
## Invite endpoints

|   |   |
|---|---|
|**Description**| Get all invites in database 
|**URL**        | `/invites`, `/invites/`, `/invites/all`
|**Method**     | `GET`
|**URL Params** | None
|**Data Params** | None 
|**Success Response** | Code: 200 Content: All found invites
|**Error Response** | Code: 500, Content: None |
|**Notes** | 

|   |   |
|---|---|
|**Description**| Create invite
|**URL**        | `/invites/`, `/invites/create`
|**Method**     | `POST`
|**URL Params** | None
|**Data Params** | ```{"eventId": Long, "name": String, "info": String, "coming": Boolean}```
|**Success Response** | Code: 201, Content: None
|**Error Response** | Code: 400, Content: JSON list of errors in english <br> Code: 500, Content: None
|**Notes** | Always sets `coming: false`

|   |   |
|---|---|
|**Description**| Edit invites
|**URL**        | `/invites/{id}`, `/invites/edit/{id}`
|**Method**     | `PUT`
|**URL Params** | Id of event
|**Data Params** | ```{"creatorId": String, "eventName": String, "eventTime": Long, "eventPlace": String, "info": String, "inviteExpire": Long } ```
|**Success Response** | Code: 200, Content: `{"inviteId": Long}`
|**Error Response** | Code: 400, Content: JSON list of errors in english <br> Code: 404, Content: `{error: "Invite not found"}` <br> Code: 500, Content: None
|**Notes** | 

|   |   |
|---|---|
|**Description**| Delete invite from database by id
|**URL**        | `/invites/{id}`, `/invites/delete/{id}`
|**Method**     | `DELETE`
|**URL Params** | Id of invite
|**Data Params** | None
|**Success Response** | Code: 200, Content: None
|**Error Response** | Code: 404, Content: `{error: "Invite not found"}` <br> Code: 500, Content: None
|**Notes** | 

