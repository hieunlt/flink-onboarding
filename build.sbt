ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

ThisBuild / resolvers ++= Seq(
  "Confluent's Maven Repository" at "https://packages.confluent.io/maven/"
)

lazy val root = (project in file("."))
  .settings(
    name := "flink-onboarding"
  )

val flinkVersion = "1.16.1"
val hudiVersion = "0.14.0"
//val deltaConnectorsVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % Provided,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % Provided,
  "org.apache.flink" % "flink-table-api-java-bridge" % flinkVersion % Provided,
  "org.apache.flink" %% "flink-table-planner" % flinkVersion,
//  "org.apache.flink" % "flink-connector-kafka" % flinkVersion,
  "org.apache.flink" % "flink-avro" % flinkVersion,
  "org.apache.flink" % "flink-avro-confluent-registry" % flinkVersion,
  "org.apache.flink" % "flink-clients" % flinkVersion,
  "org.apache.hudi" % s"hudi-flink${flinkVersion.take(4)}-bundle" % hudiVersion,
  "org.apache.hudi" % "hudi-common" % hudiVersion,
  "org.slf4j" % "slf4j-simple" % "2.0.10",
  "org.apache.hadoop" % "hadoop-common" % "3.3.4",
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "3.3.2",
//  "org.apache.iceberg" % "iceberg-flink-runtime-1.16" % "1.4.3"
//  "io.delta" % "delta-flink" % deltaConnectorsVersion,
//  "io.delta" %% "delta-standalone" % deltaConnectorsVersion,
//  "org.apache.flink" % "flink-clients" % flinkVersion,
//  "org.apache.flink" % "flink-parquet" % flinkVersion,
//  "org.apache.hadoop" % "hadoop-client" % "3.3.6",
//  "org.apache.flink" % "flink-table-common" % flinkVersion % Provided,
//  "org.apache.flink" % "flink-table-runtime" % flinkVersion % Provided

//  "org.apache.hudi" % "hudi-common" % "0.14.0",
  //  "org.apache.spark" %% "spark-core" % "3.4.2" % Provided
  //  ,
//    "org.apache.spark" %% "spark-sql" % "3.4.2" % Provided,
//  "org.codehaus.janino" % "commons-compiler" % "3.1.9"
)

//excludeDependencies ++= Seq(
//  ExclusionRule("org.codehaus.janino", "commons-compiler"),
//)