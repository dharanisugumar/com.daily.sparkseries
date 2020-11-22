package com.akamai.nsecore.util



class AppOptions[T] extends ConfigOption {

    /**
     * The config parameters defining the job details of the application.
     */

    val JOB_NAME: ConfigOption[String] = key("job.name").defaultValue("openApi").withDescription("Job Name")

    val JOB_DESCRIPTION: ConfigOption[String] = key("job.description").defaultValue("openApi").withDescription("Job Desc")

    val APP_ID: ConfigOption[String] = key("application.id").defaultValue("1").withDescription("Job Application Id")

    val PROCESS_ID: ConfigOption[String] = key("process.id").defaultValue("1").withDescription("Process Id")

}

@TO_DO: