# logregator
A log aggregation filter designed to run in AWS Lambda and convert AWS ELB Access logs into a stream of Apache Kafka events.

logregator parses AWS ELB access logs and throws away some high cardinality data such as source IPs or user agents. It later creates JSON evnts and streams them to Apache Kafka topic. This topic can be later consumed by an analytics DB, such as Druid.
