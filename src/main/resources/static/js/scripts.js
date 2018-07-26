$(".answer-write input[type=submit]").click(addAnswer);

function addAnswer(e){
    e.preventDefault(); //다음 일어날 event를 막는다.
    console.log("addAnswer click");

    var queryString = $(".answer-write").serialize();
    console.log("query : " + queryString);

    var url = $(".answer-write").attr("action");
    console.log("url : " + url);

    $.ajax({
        type : 'POST',
        url : url,
        data : queryString,
        dataType : 'json',
        error : onError,
        success : onSuccess
    });
}

function onError(){
    console.log("error");
}

function onSuccess(data, status){
    console.log(data);
    var answerTemplate = $("#answerTemplate").html();
    var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents, data.id);
    $(".qna-comment-slipp-articles").prepend(template);

    $("textarea[name=contents]").val("");
}

//답글 삭제 기능
$(".link-delete-article").click(deleteAnswer)

function deleteAnswer(e){
    e.preventDefault();

    var url = $(this).attr("href");
    console.log("url : " + url);

}

//tempalate에 데이터 매칭함수
String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};
