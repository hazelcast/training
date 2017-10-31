$(window).scroll(function() {
  if($(this).scrollTop() != 0) {
    $('#to-top').fadeIn(); 
  } else {
    $('#to-top').fadeOut();
  }
});

$('#to-top').click(function() {
  $('body,html').animate({scrollTop:0},"fast");
});