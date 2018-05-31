
var wsHost = "ws://localhost:80";
var webSocket = null;
if ("WebSocket" in window) {
    webSocket = new WebSocket(wsHost + "/webSocketServer");
    webSocket.onerror = function(event) {
    };
    webSocket.onopen = function(event) {
        console.log("open")
    };
    webSocket.onmessage = function(event) {
        var data = event.data;
        setData(data);
    };
    webSocket.onclose = function(event) {
    };
    window.onbeforeunload = function(event) {
        webSocket.close();
    };
}

function setData(data) {
    data = JSON.parse(data);
    console.log(data);
    switch (data.type) {
        case "basicMsg" : setBasicMsg(data.data);break;
        case "borrowAndReturnBackMgs" : setBorrowAndReturnBackMgs(data.data);break;
        case "borrowMaxTimeBook" : setBorrowMaxTimeBook(data.data);break;
        case "notice" : setNotice(data.data);break;
    }
}

var basicMsg = echarts.init(document.getElementById('basicMsg'));
var borrowAndReturnBackMgs = echarts.init(document.getElementById('borrowAndReturn'));
var borrowMaxTimeBook = echarts.init(document.getElementById('borrowMaxTimeBook'));
var basicMsgOption = {
    title: {
        text: '系统基本数据'
    },
    tooltip: {},
    legend: {
        data:['数量']
    },
    xAxis: {
        data: ["总数","借出","超期未还","等待归还","用户数量","借阅总量"]
    },
    yAxis: {},
    series: [{
        name: '数量',
        type: 'bar',
        data: [11, 11, 15, 13, 12, 13]
    }]
};
var borrowAndReturnBackMgsOption = {
    title: {
        text: '近七天借还统计',
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['借阅','归还']
    },
    toolbox: {

    },
    xAxis:  {
        type: 'category',
        boundaryGap: false,
        data: ['5/28','5/29','5/30','5/31','6/1','6/2','6/3']
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value}'
        }
    },
    series: [
        {
            name:'借阅',
            type:'line',
            data:[11, 11, 15, 13, 12, 13, 10],
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        },
        {
            name:'归还',
            type:'line',
            data:[5, 20, 23, 12, 13, 2, 8],
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}

                ]
            }
        }
    ]
};
var borrowMaxTimeBookOption = {
    title : {
        text: '借阅时间统计（小时）',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: []
    },
    series : [
        {
            name: '借阅时间',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
                {name:'诛仙',value : 350},
                {name:'诛仙2',value : 310},
                {name:'诛仙之为爱成神',value : 280},
                {name:'诛仙后续',value : 100},
                {name:'其他',value : 500}
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};




/*
 {
    type:'borrow|returnBack',
    member:'周东生',
    bookInfo:'诛仙',
    time:'2018-05-30 12:22:33'

 }
  */
function setNotice(notice) {
    var li;
    if ("borrow" === notice.type) {
        li = $(`<li class="list-group-item borrow"><span class="time">${notice.time}</span><span class="member">${notice.member}</span>借阅图书<span class="bookInfo">${notice.bookInfo}</span></li>`);
    } else if ("returnBack" === notice.type) {
        li = $(`<li class="list-group-item notice_returnBack"><span class="time">${notice.time}</span><span class="member">${notice.member}</span>归还图书<span class="bookInfo">${notice.bookInfo}</span></li>`);
    }
    var ul = $("#noticeMsg");
    if (ul.children(".list-group-item").length > 6) {
        $(ul.children(".list-group-item")[0]).css("display","none");
        $(ul.children(".list-group-item")[0]).remove();
    }
    ul.append(li);
}
/*
 * data = {index:0,value:10}
 * */
function setBasicMsg(data) {
    basicMsgOption.series[0]["data"][data.index] = data.value;
    basicMsg.setOption(basicMsgOption);
}
/*
data = {index : 0,time : '6/3',borrow : 11,returnBack : 5}

* */
function setBorrowAndReturnBackMgs(data) {
    borrowAndReturnBackMgsOption.xAxis["data"][data.index] = data.time;
    borrowAndReturnBackMgsOption.series[0]["data"][data.index] = data.borrow;
    borrowAndReturnBackMgsOption.series[1]["data"][data.index] = data.returnBack;
    borrowAndReturnBackMgs.setOption(borrowAndReturnBackMgsOption);
}
/*
 data = [
    {name:'诛仙',value : 350},
    {name:'诛仙2',value : 310},
    {name:'诛仙之为爱成神',value : 280},
    {name:'诛仙后续',value : 100},
    {name:'其他',value : 500}
 ]
* */
function setBorrowMaxTimeBook(data) {
    var title = new Array();
    for (var i = 0;i < data.length;i ++) {
        title.push(data[i].name);
    }
    borrowMaxTimeBookOption.legend["data"] = title;
    borrowMaxTimeBookOption.series[0]["data"] = data;
    borrowMaxTimeBook.setOption(borrowMaxTimeBookOption);
}