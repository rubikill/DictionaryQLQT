$(document).ready(function () {
    var mean = $('#mean').text();
    $('#mean').html(createTextSelect(mean));

    var example = $('#example').text();
    $('#example').html(createTextSelect(example));

    var dyknow = $('#dyknow').text();
    $('#dyknow').html(createTextSelect(dyknow));

    $('.word-select').click(function () {
        var text = $(this).text();
        $(this).addClass("selected");
        // call android search word
        window.android.search(text);

        //delay selected
        $(this).delay(800);
        $(this).queue(function () {
            $(this).removeClass("selected");
            $(this).dequeue();
        });
    });
    window.android.onLoadComplete();
});

function createTextSelect(text) {
    var result = '';
    var words = text.split(' ');
    for (var i = 0; i < words.length; i++) {
        var word = words[i];
        var strs = word.match(/[a-zA-Z]{2,}/);
        if (strs != null) {
            var str = strs[0];
            if (str.length == word.length) {
                result += "<span class='word-select'>" + str + "</span> ";
            }
            else {
                var start = word.indexOf(str);
                result += word.slice(0, start) + "<span class='word-select'>" + str + "</span>" + word.slice(start + str.length) + " ";
            }
        }
        else {
            result += word;
        }
    }

    return result;
}