package com.akamai.nsecore.service

import com.akamai.nsecore.Connectors.{BasicSinkConnector, BasicSourceConnector}
import com.akamai.nsecore.common.{FlinkExecutor, FlinkExecutorTask}
import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.util.OptionalFailure
import org.rogach.scallop.ScallopConf
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import scala.beans.BeanProperty


class ProcessLoadData[A] extends FlinkExecutorTask {
  @BeanProperty var silentMode: Boolean = true
  private lazy val log = LoggerFactory.getLogger(this.getClass)
  private var tableEnv: ExecutionEnvironment = _

  override def task(env: Either[StreamExecutionEnvironment, ExecutionEnvironment], args: Array[String])
                   (implicit ctx: ApplicationContext): FlinkExecutor = {

    val cmdLine = new ArgsVerify(args)
    cmdLine.verify()
    tableEnv = env.right.get
    val source = ctx.getBean(cmdLine.source()).asInstanceOf[BasicSourceConnector[A]]
    val sourceStream: DataSet[A] = source.generate(tableEnv)
    val count = sourceStream.asInstanceOf[DataSet[A]].count()
    val sink = ctx.getBean(cmdLine.sink()).asInstanceOf[BasicSinkConnector[A]]
    sink.loader(tableEnv, sourceStream.asInstanceOf[DataSet[A]])


    () => {
      if (silentMode && count <= 0) {
        new JobExecutionResult(tableEnv.getId, 0L, new java.util.HashMap[String, OptionalFailure[AnyRef]]())
      } else {
        tableEnv.execute(cmdLine.jobName())
      }
    }
    }



  class ArgsVerify(args: Array[String]) extends ScallopConf(args) {
    banner(
      """Usage :
        |--class ProcessLoadData --
        |
        |""".stripMargin)
    val jobName = opt[String](
      "job_name",
      descr = "Name of Job", default = Option("ProcessLoadDataJob"), required = false)
    val source = opt[String](
      "source",
      descr = "Spring context bean name representing the source for the job", required = true)
    val sink = opt[String]("sink",
      descr = "Spring context bean name representing the sink for the job", required = true)

  }

}

