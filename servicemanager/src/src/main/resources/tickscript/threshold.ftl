batch
    |query('''
        ${alertRule.queryQl}
    ''')
        .period(${alertRule.pastTime})
        .every(${alertRule.pastTime})
        ${groupBy}
    |alert()
        .id('{{ .Name }}/{{ index .Tags "appId" }}/{{ index .Tags "host" }}')
        .message('${alertMessage}')
        .${level}(lambda: ${lambda} )
        .log('${logpath}')
        .post('http://sm.mapps.ip:8761/logalert?ruleId=${alertRule.id}&key=${smsPushKey}')
        ${email}${sms}