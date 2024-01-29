import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import org.apache.avro.Schema
import org.apache.flink.table.api
import org.apache.flink.table.types.DataType

import scala.collection.JavaConverters._

case class SchemaConf(schemaRegistryUrl: String, subject: String, version: Int)

class SchemaHelper(conf: SchemaConf) {
  private val schemaClient = new CachedSchemaRegistryClient(conf.schemaRegistryUrl, 50)
  private val schemaVersion = schemaClient.getByVersion(conf.subject, conf.version, true)
  val avroSchema = new Schema.Parser().parse(schemaVersion.getSchema)

  def avroToTableSchema(): api.Schema = {
    val builder = api.Schema.newBuilder()

    for (field <- avroSchema.getFields.asScala) {
      val fieldName = field.name
      val fieldType = avroToDataType(field.schema)

      builder.column(fieldName, fieldType)
    }
    builder.build()
  }

  private def mapUnionToDataType(avroFieldType: Schema): DataType = {
    val firstType = avroFieldType.getTypes.get(0)
    val secondType = avroFieldType.getTypes.get(1)
    if (firstType.getType == Schema.Type.NULL) {
      return avroToDataType(secondType)
    }
    if (secondType.getType == Schema.Type.NULL) {
      return avroToDataType(firstType)
    }
    throw new UnsupportedOperationException("Unsupported Avro type: " + avroFieldType.getType)

  }

  private def avroToDataType(avroFieldType: Schema): DataType = {
    avroFieldType.getType match {
      case Schema.Type.RECORD => api.DataTypes.ROW()
      case Schema.Type.ENUM => api.DataTypes.STRING()
      case Schema.Type.ARRAY => api.DataTypes.ARRAY(api.DataTypes.STRING())
      case Schema.Type.MAP => api.DataTypes.MAP(api.DataTypes.STRING(), api.DataTypes.STRING())
      case Schema.Type.UNION => mapUnionToDataType(avroFieldType)
      case Schema.Type.FIXED => api.DataTypes.STRING()
      case Schema.Type.STRING => api.DataTypes.STRING()
      case Schema.Type.BYTES => api.DataTypes.BYTES()
      case Schema.Type.INT => api.DataTypes.INT()
      case Schema.Type.LONG => api.DataTypes.BIGINT()
      case Schema.Type.FLOAT => api.DataTypes.FLOAT()
      case Schema.Type.DOUBLE => api.DataTypes.DOUBLE()
      case Schema.Type.BOOLEAN => api.DataTypes.BOOLEAN()
      case Schema.Type.NULL => api.DataTypes.NULL()
      case _ => throw new UnsupportedOperationException("Unsupported Avro type: " + avroFieldType.getType)
    }
  }
}
