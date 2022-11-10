SELECT pubRequestId,
       invoiceDate,
       pubStatId,
       statTypeId,
       statType,
       isCummulativeStat,
       isCpCodeStat,
       stat
       from nsePub.`aisAggStat`
       WHERE requestId = ?
