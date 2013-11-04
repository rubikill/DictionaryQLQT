$(".mean").click(function () {
    window.android.callAndroid($(this).text())
});

$(".audio").click(function () {
    window.android.callEventAAudio($(this).text())
});

$(".favorite").click(function () {
	$(this).attr('src', 'file:///android_asset/definition-favorite-star-on.png')
    window.android.callEventAddFavorite($(this).text())
});