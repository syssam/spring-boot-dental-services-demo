jQuery(function() {
    initValidator();
    
    $( "table:not(.disable-table-responsive)" ).each(function() {
        if(!$(this).parent().hasClass('table-responsive')) {
            $(this).addClass('table').wrap( "<div class='table-responsive'></div>" );
        }
    })

	$('#menu-trigger').on('click', function (e) {
		e.preventDefault();
		if ($('#mobile-nav').hasClass('open')) {
			$('#mobile-nav').removeClass('open');
			$('#menu-trigger').removeClass('open').find('i').addClass('fa-bars').removeClass('fa-times');
			$('body').removeClass('open');
		} else {
			$('#mobile-nav').addClass('open');
			$('#menu-trigger').addClass('open').find('i').addClass('fa-times').removeClass('fa-bars');
			$('body').addClass('open');
		}
    });

    mobileMenu();
});

function mobileMenu(){
    $('#mobile-nav .dropdown-menu > li.active').parents('.nav-item').addClass('active');
    $("#mobile-nav .nav-item.dropdown > .nav-link").on('click', function (e) {
        if($('body').hasClass('open')) {
            e.preventDefault();
            const parent = $(this).parent();
            parent.find('.dropdown-menu').slideToggle(null, null, function() {
                parent.toggleClass("active");
                parent.find('.dropdown-menu').removeAttr("style");
            });
            const activeParent = $("#mobile-nav .nav-item.dropdown.active").not(parent);
            activeParent.find('.dropdown-menu').slideToggle(null, null, function() {
                activeParent.toggleClass("active");
                activeParent.find('.dropdown-menu').removeAttr("style");
            });
        }
    });
}

function initValidator() {
    jQuery.validator.setDefaults({
        errorElement: 'div',
        errorPlacement: function (error, element) {
            error.addClass('invalid-feedback');
            element.closest('.form-group').append(error);
        },
        highlight: function (element, errorClass, validClass) {
            var name = element.getAttribute('name');
            if($(element).is(':radio')) {
                $(element).closest('.form-group').find('input[name="'+name+'"]:radio').addClass('is-invalid');
            } else if($(element).is(':checkbox')) {
                $(element).closest('.form-group').find('input[name="'+name+'"]:checkbox').addClass('is-invalid');
            } else {
                $(element).addClass('is-invalid');
            }
        },
        unhighlight: function (element, errorClass, validClass) {
            var name = element.getAttribute('name');
            if($(element).is(':radio')) {
                $(element).closest('.form-group').find('input[name="'+name+'"]:radio').removeClass('is-invalid');
            } else if($(element).is(':checkbox')) {
                $(element).closest('.form-group').find('input[name="'+name+'"]:checkbox').removeClass('is-invalid');
            } else {
                $(element).removeClass('is-invalid');
            }
        }
    });
}

function showAjaxError(result) {
    if(result.errors != undefined) {
        for (const property in result.errors) {
            Swal.fire({
                html: result.errors[property],
                customClass: {
                    container: 'notification-container',
                }
            })
            break;
        }
    } else if(result.error) {
        Swal.fire({
            html: result.error,
            customClass: {
                container: 'notification-container',
            }
        })
    } else if(result.message) {
        Swal.fire({
            html: result.message,
            customClass: {
                container: 'notification-container',
            }
        })
    }
}

function htmlConfirm(html, action) {
    Swal.fire({
        html: html,
        customClass: {
            container: 'notification-container',
        }
    }).then((result) => {
        if (result.isConfirmed && action) {
            action();
        }
    })
}

function showErrorMessage(message) {
    return Swal.fire({
        html: message,
        customClass: {
            container: 'notification-container',
        }
    })
}

function loadingHtml() {
    return '<span class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span><span class="sr-only">Loading...</span>';
}
