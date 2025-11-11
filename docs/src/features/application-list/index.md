# Application list

The application list is the main interface on the home page the project. It provides an overview of all applications submitted by citizens, allowing caseworkers
to efficiently manage and process them. It displays key information about each application, such as the applicant's name, submission date, current status, and
any relevant actions that need to be taken. The list is designed to be user-friendly, enabling caseworkers to quickly identify applications that require
attention and to navigate through the applications with ease.

![Application list showing multiple applications with their details and status](./application-list.png)

## Column contents

The displayed columns represent the most important data from the citizen form, such as the applicant or project title. Additionally, a few additional columns
provide context and facilitate the processing of applications:

| Column               | Description                                                                             |
| -------------------- | --------------------------------------------------------------------------------------- |
| Status               | The current status of the application, indicating its stage in the processing workflow. |
| Nummer               | The Zammad ticket number associated with the application.                               |
| Aktenzeichen         | Identifier in the E-Akte system where the application is documented.                    |
| Aktualisierung       | Shows in which of the involved systems (Zammad, EAkte) the last activity occurred.      |
| Datum Aktualisierung | The date when the last update was made to the application.                              |

## Updating application status

The application status can be updated directly from the application list. Caseworkers can select an application and change its status based on the current stage
of processing. This feature streamlines the workflow by allowing quick updates without needing to navigate to a separate page for each application.

![Application list with an open dropdown menu to update the application status](./application-list-update-status.png)
