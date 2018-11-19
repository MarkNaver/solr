<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../inc/pagebegin.jsp" %>

<script>
    $(function () {

        $("#searchBtn").on('click', function (event) {
            var params = $('#solrDemo').serialize();//序列化表单
            $.ajax({
                type: "post",
                url: "testQuery",
                dataType: "json",
                data: params,
                success: function (data) {
                    if (data.result == 'SUCCESS') {
                        $("#totalNum").text(data.total);
                        $("#dataDiv1").empty();
                        for (var i = 0; i < data.rows.length; i++) {
                            $("#dataDiv1").append("ID:" + data.rows[i].id + "===标题：" + data.rows[i].title + "===价格：" + data.rows[i].price + "</br>");
                        }

                    }

                }
            })

        })

        $("#separateBtn").on('click', function (event) {
            var params = $('#solrDemo').serialize();//序列化表单
            $.ajax({
                type: "post",
                url: "testSeparateWord",
                dataType: "json",
                data: params,
                success: function (data) {
                    if (data.result == 'SUCCESS') {
                        $("#dataDiv2").empty();
                        for (var i = 0; i < data.jsonArray.length; i++) {
                            $("#dataDiv2").append(data.jsonArray[i] + ",");
                        }

                    }

                }
            })

        })
    })

</script>

<body>
${msg}

<form id="solrDemo">
    <h2>搜索功能：</h2>
    <hr>
    <input type="text" id="keyValue" name="keyValue" placeholder="请输入关键字！">
    <select id="sortProperty" name="sortProperty">
        <option value=""></option>
        <option value="id">id</option>
        <option value="title">title</option>
        <option value="price">price</option>
    </select>
    <input type="button" id="searchBtn" name="searchBtn" value="搜索"/>
    </br>
    数据总条数为：<span id="totalNum"></span>
    <div id="dataDiv1"></div>

    <h2>分词功能：</h2>
    <hr>
    <textarea rows="3" cols="80" id="sentence" name="sentence" placeholder="请输入...！"></textarea>
    <input type="button" id="separateBtn" name="separateBtn" value="分词"/>
    <div id="dataDiv2"></div>
</form>
</body>
</html>
