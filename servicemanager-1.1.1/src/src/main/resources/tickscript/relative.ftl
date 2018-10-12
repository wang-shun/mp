var data = batch
    |query('''
        ${alertRule.queryQl}
    ''')
        .period(${alertRule.pastTime})
        .every(${alertRule.pastTime})
        ${groupBy}
    |eval(lambda: "${alertRule.func}")
        .as('value')

var past = data
    |shift(${shift})

var current = data

var trigger = past
    |join(current)
        .as('past', 'current')
    |eval(lambda: ${changeLambda} ) 
        .keep()
        .as('value')
    |alert()
        .id('{{ .Name }}/{{ index .Tags "appId" }}/{{ index .Tags "host" }}')
        .message('${alertMessage}')
        .${level}(lambda: ${lambda} )
        .log('${logpath}')
        .post('http://sm.mapps.ip:8761/logalert?ruleId=${alertRule.id}&key=${smsPushKey}')
        ${email}${sms}