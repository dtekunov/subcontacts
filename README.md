Store Contacts and Get 2nd-degree Contacts
==========================================

Similarly to other social networks like Facebook or LinkedIn, [XING](https://xing.com) allows users to connect with each other. To facilitate networking, XING provides contact recommendations (see for example: [xing.com/network](https://www.xing.com/network) - _Grow your network today_):

_2nd-degree contacts_ play an important role in this recommender system. The intuition behind this is: _The friends of my friends are also my friends_.

As part of this assignment, we would like to develop a service that provides the following functionalities:

1. a method for storing contacts of a user (in-memory)
2. a method for retrieving the contacts of the user's contacts

These endpoints should be made available via the following two endpoints.

### 1. POST /user/:user_id/contacts

This endpoint allows for storing the contacts of the given user (`user_id`) and should work as follows:

```
curl -XPOST --header "Content-Type: application/json" \
  --data '{"contacts":[2, 3, 4, 5]}' \
  'http://localhost:9000/user/1/contacts' 
```

With the request above, we would store (overwrite) the contacts of user `1`. The data should be stored in-memory and forms the basis for the second endpoint.

### 2. GET /user/:user_id/sd-contacts

This endpoint returns the second-degree contacts of the given user (`user_id`), i.e. the contacts of the user's contacts.

Example: Imagine that the following contact data is already stored.

| user_id | contacts     |
|:--------|:-------------|
| 1       | [2, 3, 4, 5] |
| 2       | [1, 6, 8]    |
| 3       | [1, 4]       |
| 4       | [1, 3]       |
| 5       | [1, 6]       |
| ...     | ...          |

Given this data, we would expect the following response:

```
curl -XGET 'http://localhost:9000/user/1/sd-contacts'

Expected response: 
[6, 8]
```

Hence, `/user/:user_id/sd-contacts` should only return 2nd-degree contacts: Direct contacts of the  user (e.g. user 3 and 4) as well as the user herself (e.g. 1) should not be returned.

### Testing

In order to test your service, you can use the test data that is given in [contacts.tsv](contacts.tsv). This file has two columns:

| Column | Description |
|:-------|:------------|
| `user_id` | the ID of the user |
| `contacts` | the contacts of the corresponding user in the JSON format that is expected by `POST /user/:user_id/contacts` |

We also provide a Jmeter file ([jmeter-test.jmx](jmeter-test.jmx)) which you can use to test your service, but you are free to create your own scripts for evaluating your service.


### Remarks

- You can develop the corresponding service either in Scala or Java.
- The service should be designed to be fast and should also work in case there are concurrent requests.
- We do not expect a sophisticated solution, but we expect that you can justify your design decisions thoroughly.
- In a next meeting, we would like to discuss your solution. Please push your code and descriptions before the meeting :-)
