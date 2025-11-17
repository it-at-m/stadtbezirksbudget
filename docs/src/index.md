---
# https://vitepress.dev/reference/default-theme-home-page
layout: home

hero:
  name: "Stadtbezirksbudget"
  tagline: "This is the documentation for the Stadtbezirksbudget project"
  actions:
    - theme: brand
      text: About
      link: /about
    - theme: alt
      text: Architecture
      link: /architecture/
features:
  - icon: 📋
    title: Application list
    details: The application list on the homepage provides a summary of all applications submitted by citizens, enabling caseworkers to manage and process them efficiently.
    link: /features/application-list/
  - icon: 2️⃣
    title: Title 2
    details: Add some interesting information here
  - icon: 📨
    title: Reliable communication
    details: The project uses a robust communication architecture to ensure reliable data exchange between all connected systems. To achieve this, the event bus Kafka is used.
    link: /features/reliable-communication
---
