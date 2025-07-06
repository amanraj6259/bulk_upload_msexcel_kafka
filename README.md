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
| **Custom parameters**        | Extra configs like compression type, specific retention bytes, etc. Advanced tuning.                                                                                       | Example: `compression.type=gzip`                                                            | Kafka topic config (UI or terminal)                                           |
