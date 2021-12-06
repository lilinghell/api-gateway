export const api_delete = `swagger: "2.0"
info:
  description: "{{description}}"
  version: "{{version}}"
  title: "{{title}}"
paths:
  {{url}}:
    delete:
      tags:
      - "pet"
      operationId: "demo get"
      parameters:
      - name: "id"
        in: "query"
        description: "id"
        required: true
        type: "integer"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/ApiResponse"
definitions:
  ApiResponse:
    type: "object"
    required:
      - "code"
    properties:
      code:
        type: "string"
      msg:
        type: "string"
      data:
        type: "string"`
export const api_put = `swagger: "2.0"
info:
  description: "{{description}}"
  version: "{{version}}"
  title: "{{title}}"
paths:
  {{url}}:
    put:
      operationId: "demo put"
      tags:
        - pet
      parameters:
      - in: "body"
        name: "body"
        required: true
        schema:
          $ref: "#/definitions/Pet"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/ApiResponse"
definitions:
  Pet:
    type: "object"
    required:
    - "name"
    - "status"
    properties:
      id:
        type: "integer"
        format: "int64"
        description: "id"
      name:
        type: "string"
        example: "doggie"
        description: "名称"
      status:
        type: "string"
        description: "状态"
        enum:
        - "available"
        - "pending"
        - "sold"
  ApiResponse:
    type: "object"
    required:
      - "code"
    properties:
      code:
        type: "string"
      msg:
        type: "string"
      data:
        $ref: "#/definitions/Pet"`
export const api_get = `swagger: "2.0"
info:
  description: "{{description}}"
  version: "{{version}}"
  title: "{{title}}"
paths:
  {{url}}:
    get:
      tags:
      - "pet"
      operationId: "findPetsByStatus"
      parameters:
      - name: "status"
        in: "query"
        description: "状态"
        required: true
        type: "array"
        items:
          type: "string"
          enum:
          - "available"
          - "pending"
          - "sold"
          default: "available"
        collectionFormat: "multi"
      responses:
        "200":
          description: "successful operation"
          schema:
            $ref: "#/definitions/ApiResponse"
definitions:
  Pet:
    type: "object"
    required:
    - "name"
    - "status"
    properties:
      id:
        type: "integer"
        format: "int64"
        description: "id"
      name:
        type: "string"
        example: "doggie"
        description: "名称"
      status:
        type: "string"
        description: "状态"
        enum:
        - "available"
        - "pending"
        - "sold"
  ApiResponse:
    type: "object"
    required:
      - "code"
    properties:
      code:
        type: "string"
      msg:
        type: "string"
      data:
        type: "array"
        items:
          $ref: "#/definitions/Pet"`
export const api_post = `swagger: "2.0"
info:
  description: "{{description}}"
  version: "{{version}}"
  title: "{{title}}"
paths:
  {{url}}:
    post:
      operationId: "addPet"
      tags:
        - pet
      parameters:
      - in: "body"
        name: "body"
        required: true
        schema:
          $ref: "#/definitions/Pet"
      responses:
        "200":
          description: "add success"
          schema:
            $ref: "#/definitions/ApiResponse"
definitions:
  Pet:
    type: "object"
    required:
    - "name"
    - "status"
    properties:
      id:
        type: "integer"
        format: "int64"
        description: "id"
      name:
        type: "string"
        description: "名称"
      status:
        type: "string"
        description: "状态"
        enum:
        - "available"
        - "pending"
        - "sold"
  ApiResponse:
    type: "object"
    required:
      - "code"
    properties:
      code:
        type: "string"
      msg:
        type: "string"
      data:
        $ref: "#/definitions/Pet"`