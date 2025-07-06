 Kafka producer settings
spring.kafka.bootstrap-servers=localhost:9092
➡ The Kafka server (broker) is running locally on port 9092.
💡 Like saying: “Kafka, my app will connect to you at this address.”
spring.kafka.topic.driver-location=student-topic
➡ The Kafka topic name your app uses to send/read messages → student-topic.
💡 Example: All student data will be published/consumed from student-topic.

spring.kafka.producer.retries=2
➡ Kafka producer will retry 2 times if sending a message fails (due to network issue, etc).

💡 Example: You try to send a student message → network glitch → Kafka tries 2 more times before giving up.

spring.kafka.producer.acks=1
➡ Kafka broker will reply “OK” after leader node gets the message.

💡 Example:

acks=0 → no reply needed

acks=1 → leader got it, reply OK (your case)

acks=all → leader + followers got it before reply

🟢 Kafka consumer settings

spring.kafka.consumer.group-id=student-group
➡ All consumers with this ID will balance reading of messages.
💡 Example: You have 4 app instances → Kafka divides messages among them (load sharing).

spring.kafka.consumer.auto-offset-reset=earliest
➡ When no offset is found → Kafka reads messages from the beginning of the topic.

💡 Example: First time you start your app → it reads all old messages in student-topic.

spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
➡ Kafka converts message key (if any) to String so your app can understand it.


spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
➡ Kafka converts message value (the student data) from JSON to your Java Student object.

spring.kafka.consumer.properties.spring.json.value.default.type=com.bulkupload.model.Student
➡ Tells Kafka what class the JSON represents → Student.

💡 Example: Kafka sees JSON → converts into Student:
{"std_id":1, "name":"Aman", "email":"a@a.com", "location":"Delhi"}
➡ → Student object.

spring.kafka.consumer.properties.reconnect.backoff.ms=5000
spring.kafka.producer.properties.reconnect.backoff.ms=5000
➡ If Kafka connection fails → wait 5000 ms = 5 seconds before retrying to reconnect.

💡 Example: Kafka goes down → app waits 5 seconds → tries again.



| **Parameter**                | **Layman’s Meaning**                                                                                                                                                       | **Example**                                                                                 | **Where You Set This?**                                                       |
| ---------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------- |
| **Topic Name**               | The unique name for a queue where messages (data) go. Think of it like a folder name where you’re dropping your student messages.                                          | `student-topic`                                                                             | You define this when you create the topic (via Kafka UI or terminal command)  |
| **Partitions**               | Think of a topic as a big box divided into sections (partitions). Each section holds part of your data. More partitions = can process faster, in parallel.                 | 4 partitions → 1 for each school/location (e.g., `delhi`, `mumbai`, `bangalore`, `chennai`) | When creating the topic (UI or terminal); **can’t change easily later**       |
| **Cleanup Policy**           | What to do with old data in the topic? <br> `delete`: throw away data after some time or size <br> `compact`: keep only latest data per key                                | For student records, usually `delete` (after 7 days).                                       | When creating topic (UI or terminal). Can update later with Kafka commands.   |
| **Min In Sync Replicas**     | How many copies (replicas) of your data must confirm they saved it before Kafka says “done.” <br> Think of sending a parcel — how many receivers must confirm they got it? | 1 for dev (1 broker) <br> 2+ for production (multiple brokers)                              | Kafka topic config (UI or terminal); not in application.properties            |
| **Replication Factor**       | How many *copies* of your data Kafka should keep. This helps in case a broker (server) fails — other copies will serve the data.                                           | 1 for dev (no copy) <br> 2-3 for production                                                 | When creating the topic (UI or terminal); can’t change after topic is created |
| **Retention Time (ms)**      | How long Kafka keeps the data in the topic before deleting it. <br> Like saying: “Clear this folder every 7 days.”                                                         | `604800000` (7 days) <br> `86400000` (1 day)                                                | Set at topic creation (UI or terminal) or can change later via Kafka config   |
| **Max size on disk (GB)**    | How big (in GB) the topic can grow before Kafka starts deleting old messages (if using `delete` policy).                                                                   | e.g. `10 GB` → keep max 10 GB data at a time                                                | Topic config (UI or terminal)                                                 |
| **Max message size (bytes)** | The biggest single message Kafka will accept. Useful for large data uploads.                                                                                               | Default = 1MB (1048576 bytes)                                                               | Broker level setting; not usually set per topic or in application.properties  |
| **Custom parameters**        | Extra configs like compression type, specific retention bytes, etc. Advanced tuning.                                                                                       | 
Example: `compression.type=gzip`                                                            | Kafka topic config (UI or terminal)                                           |




Compose.yml file for docker

✅ Sure! Let’s break down your **Docker Compose file** (for Zookeeper + Kafka + Kafka-UI) in **simple, layman’s terms**.

---

# 🌟 **VERSION**

```yaml
version: '3.1'
```

➡ This tells Docker we are using **Compose syntax version 3.1**, which is compatible with modern Docker.

---

# 🌟 **SERVICES**

Your app has **3 services (or mini-apps)** that Docker will run:
1️⃣ Zookeeper
2️⃣ Kafka
3️⃣ Kafka-UI

Each service runs in its **own container (mini computer)**.

---

## 🚀 **1️⃣ Zookeeper**

```yaml
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
```

▶ **image:**
We’re using the prebuilt image `wurstmeister/zookeeper`.
👉 Think of this like a ready-made app to run Zookeeper.

▶ **container\_name:**
We’re naming the container **`zookeeper`** so it's easy to refer to.

▶ **ports:**
We are saying:

* **"2181:2181"** → Expose port 2181 on your PC, and connect it to port 2181 inside the container.
* Zookeeper uses **2181** by default so Kafka can talk to it.

💡 *Zookeeper keeps track of Kafka cluster’s metadata (brokers, topics, configs).*

---

## 🚀 **2️⃣ Kafka**

```yaml
  kafka:
    image: wurstmeister/kafka
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
```

▶ **image:**
Uses `wurstmeister/kafka` → a ready-made Kafka image.

▶ **container\_name:**
Names the container **`kafka`**.

▶ **ports:**

* **"9092:9092"** → Connect port 9092 of your PC to port 9092 inside the container.
* This lets you connect to Kafka on `localhost:9092` from your apps.

▶ **environment:**
We’re setting config values that Kafka needs:

* `KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092`
  → Kafka listens on all network interfaces on port 9092 (inside container).

* `KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092`
  → Tells clients (your apps) to connect to Kafka using `localhost:9092`.

* `KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181`
  → Tells Kafka where to find Zookeeper (our service `zookeeper` on port 2181).

💡 *Kafka brokers messages; apps can publish (send) and subscribe (read) data.*

---

## 🚀 **3️⃣ Kafka-UI**

```yaml
  kafka-ui:
    image: provectuslabs/kafka-ui
    container_name: kafka-ui
    ports:
      - "8089:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local-kafka
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: localhost:9092
    depends_on:
      - kafka
```

▶ **image:**
Uses `provectuslabs/kafka-ui` → this is a nice web app to view Kafka topics, messages, and more.

▶ **container\_name:**
We call this container **`kafka-ui`**.

▶ **ports:**

* **"8089:8080"** → Inside the container, Kafka-UI runs on port 8080. On your PC, you’ll open browser at `localhost:8089`.

▶ **environment:**
Settings for the Kafka-UI tool:

* `KAFKA_CLUSTERS_0_NAME: local-kafka`
  → We give this cluster a name ("local-kafka") in the UI.

* `KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: localhost:9092`
  → We tell Kafka-UI how to connect to Kafka (at `localhost:9092`).

▶ **depends\_on:**
Kafka-UI will wait for Kafka to start first.

💡 *Kafka-UI is like a dashboard where you can see what's going on inside Kafka.*

---

# ✅ **Summary of what happens**

👉 When you run `docker-compose up`:

* A **Zookeeper server** starts (port 2181).
* A **Kafka server** starts, talks to Zookeeper (port 9092 for client apps).
* A **Kafka-UI web app** starts (open `http://localhost:8089`).

---

💡 **Bonus Tip:** If you'd like, I can draw a simple diagram for this setup or help you add persistence (data saved on disk)!

Do you want that? 🚀
