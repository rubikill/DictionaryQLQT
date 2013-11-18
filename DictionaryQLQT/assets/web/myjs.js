﻿var headertp = "<div id='headerword'>\
            <span class='word'>@WORD</span>\
            <span class='audio' id='audio'></span>\
            <span class='favorite' id='favorite' data-starred='false'></span>\
            <span class='pron'>@PRON</span>\
        </div>";

var functp = "<div class='func'>@FUNC</div>";

var meaningtp = "<div>\
            <div class='dot'></div>\
            <span>@MEAN</span>\
        </div>";

var examtp = "<div class='exam'>\
            <div class='dot1'></div>\
            @EX:\
            <span>@TRANS</span>\
        </div>";

$(document).ready(function () {
    var mean = $('#mean').text();
    $('#mean').html('');
    var lines = mean.split('\n');
    for (var i = 0; i < lines.length; i++) {
        var line = lines[i].trim();
        if (line == '') continue;
        var firtChar = line[0];
        switch (firtChar) {
            case '@':
                // tao header
                createHeader(line);
                break;
            case '*':
                $(functp.replace('@FUNC', line.substring(1, line.length).trim())).appendTo('#mean');
                break;
            case '-':
                $(meaningtp.replace('@MEAN', line.substring(1, line.length).trim())).appendTo('#mean');
                break;
            case '=':
                createExam(line);
                break;
            case '!':
                var text = line + lines[i + 1];
                createExam(text);
                i++;
                break;
        }
    }


    $('.example').click(function () {
		$(this).addClass("selected");		
		// call android search word
        window.android.search($(this).text());
		
		//delay selected
		$(this).delay(800);
		$(this).queue( function() {
		  $(this).removeClass("selected");
		  $(this).dequeue();
		});
    });

    $('#audio').click(function () {
        window.android.speakOut($('.word').text());
    });

    $('#favorite').click(favoriteClick);
	window.android.onLoadComplete();
});

function setFavorite(isFavorite){
	var starred = $('#favorite').attr('data-starred') == 'true' ? true : false;
	if (starred == isFavorite) return;
	if (isFavorite){
		$('#favorite').addClass('starred');
		$('#favorite').attr('data-starred', true);
	} else {
		$(this).removeClass('starred');
		$('#favorite').attr('data-starred', false);
	}
}

function favoriteClick(){
	var word = $('.word').text();
    var starred = $(this).attr('data-starred') == 'true' ? true : false;
    if (starred) {
        $(this).removeClass('starred');
		window.android.removeFavorite(word);
    } else {
        $(this).addClass('starred');
		window.android.setFavorite(word);
    }
    $(this).attr('data-starred', !starred);
}

function createHeader(text) {
    var index = text.indexOf('/', 0);
    var word, pron;
    if (index != -1) {
        word = text.substring(1, index).trim();
        pron = text.substring(index, text.length).trim();
    } else {
        word = text.substring(1, text.length).trim();
        pron = '';
    }
    var html = headertp.replace('@WORD', word).replace('@PRON', pron);
    $(html).appendTo('#mean');
}

function createExam(text) {
    var mark = (text[0] == '=') ? '+' : '-';
    var index = text.indexOf(mark);

    var ex = '';
    var exText = text.substring(1, index).trim();
    var words = exText.split(' ');
    for (var i = 0; i < words.length; i++) {
        var word = words[i];
        if (word == '') continue;
        ex += "<span class='example'>" + word + " </span>";
    }

    var trans = text.substring(index + 1, text.length).trim();

    var html = examtp.replace('@EX', ex).replace('@TRANS', trans);
    $(html).appendTo('#mean');
}