import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.table.data.{GenericRowData, RowData, StringData, TimestampData}
import org.apache.hudi.common.model.HoodieTableType
import org.apache.hudi.configuration.FlinkOptions
import org.apache.hudi.util.HoodiePipeline

import java.util.Random
import scala.collection.JavaConverters.mapAsJavaMapConverter


object DemoHudi extends App {
  val targetTable = "hudi"
  val basePath = s"file:///Users/nguyenlethienhieu/Projects/flink-onboarding/data/$targetTable"
  val options = Map.apply(
    FlinkOptions.PATH.key() -> basePath,
    FlinkOptions.TABLE_TYPE.key() -> HoodieTableType.COPY_ON_WRITE.name(),
    FlinkOptions.PRECOMBINE_FIELD.key() -> "ts",
  ).asJava
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  private val sequenceStream = env.fromSequence(1, 100000)
  private val dataStream = sequenceStream.map(x => longToRowData(x))


  val builder = HoodiePipeline.builder(targetTable)
    .column("id INT")
    .column("data BIGINT")
    .column("ts TIMESTAMP(3)")
    .column("`partition` VARCHAR(2)")
    .pk("id")
    .partition("partition")
    .options(options)

  builder.sink(dataStream, false)

//  dataStream.print()
  env.execute()

  private def longToRowData(value: Long): RowData = {
    val rand = new Random
    val id = rand.ints(0, 10).findFirst().getAsInt
    val partition = rand.ints(0, 5).findFirst().getAsInt
    val ts = System.currentTimeMillis()
    val data = new GenericRowData(4)
    data.setField(0, id)
    data.setField(1, value)
    data.setField(2, TimestampData.fromEpochMillis(ts))
    data.setField(3, StringData.fromString(f"${partition}%02d"))
    data
  }
}
