# logregator
A log aggregation filter designed to run in [AWS Lambda](https://aws.amazon.com/lambda/) and convert AWS ELB access logs into a stream of Apache Kafka events.

logregator parses AWS ELB access logs and throws away some high cardinality data such as source IPs or user agents. It later creates JSON evnts and streams them to [Apache Kafka](https://kafka.apache.org/) topic. This topic can be later consumed by an analytics DB, such as Druid (using driod-io [tranquility)](https://github.com/druid-io/tranquility/blob/master/docs/kafka.md).
