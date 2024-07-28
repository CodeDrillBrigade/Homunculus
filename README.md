# Homunculus

![Homunculus Icon](https://raw.githubusercontent.com/CodeDrillBrigade/Homunculus-desk/main/public/logo192.png)

Homunculus is a simple app to manage biochemistry lab inventories.<br>
It supports:

-   Multiple users with different permissions
-   Managing multiple rooms with multiple cabinets, with a shelf granularity.
-   Managing different types of materials with different configuration, allowing to specify the structure of each box.
-   Updating the current inventory, keeping a History of the usage of each material.

## :warning: Running Homunculus

This is only the Kotlin-based backend of Homunculus. To run the whole application, check the [full-fledged compose repo](https://github.com/CodeDrillBrigade/homunculus-compose).<br>
If for some reason you want to build this repo as a standalone, bear in mind that you need a running instance of [Hermes](https://github.com/LotuxPunk/Hermes) to send the invitation and password reset emails.<br>
Apart from this, you need to:

-   Clone the repo.
-   Define the following environment variables either in your run configuration or in a `.env` file to use with the docker image:

```
AUTH_SECRET= #Authentication secret for the JWT
REFRESH_SECRET= #Refresh secret for the HWT
MONGODB_USERNAME= #The username of your MongoDB user for your db
MONGODB_PASSWORD= #The password for your MongoDB user
MONGODB_IP= #The IP of your MongoDB instance
MONGODB_PORT= #The port of your MongoDB instance
MONGODB_DATABASE= #The name your database
HOMUNCULUS_URL= #The base URL of your frontend
HERMES_URL= #The URL of a running instance of Hermes
RESET_PASSWORD_TEMPLATE_ID= #The template ID of the Hermes template of the password reset email
INVITE_TEMPLATE_ID= #The template ID of the Hermes template of the invitation email
```

-   Run it

You can use the provided Dockerfile to build an image for it without needing modifications:

```bash
docker build . -t homunculus:your-local-version
```
