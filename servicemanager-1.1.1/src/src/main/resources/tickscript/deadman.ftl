batch
    |query('''
        ${alertRule.queryQl}
    ''')
        .period(${alertRule.pastTime})
        .every(${alertRule.pastTime})
        ${groupBy}
    |stats(${deadtime})
        .align()
    |derivative('emitted')
        .unit(${deadtime})
        .nonNegative()
    |alert()
        .id('{{ .Name }}/{{ index .Tags "appId" }}/{{ index .Tags "host" }}')
        .message('${alertMessage}')
        .stateChangesOnly()
        .${level}(lambda: "emitted" <= 0.0 )
        .log('${logpath}')
        .post('http://sm.mapps.ip:8761/logalert?ruleId=${alertRule.id}&key=${smsPushKey}')
        ${email}${sms}