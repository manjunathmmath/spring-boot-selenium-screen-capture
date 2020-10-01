<%@ include file="../common/header.jspf" %>
<body class="hold-transition sidebar-mini">

<div class="wrapper">
    <%@ include file="../common/navigation.jspf" %>
    <%@ include file="../common/sidebar.jsp" %>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <div class="container-fluid">
                <div class="row mb-2">
                    <div class="col-sm-6">
                        <h1>Selenium Capture Screenshot</h1>
                    </div>
                    <div class="col-sm-6">
                        <ol class="breadcrumb float-sm-right">
                            <li class="breadcrumb-item"><a href="#">Home</a></li>
                            <li class="breadcrumb-item active">Selenium</li>
                        </ol>
                    </div>
                </div>
            </div><!-- /.container-fluid -->
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-12">
                        <!-- Horizontal Form -->
                        <div class="card card-info">
                            <div class="card-header">
                                <h3 class="card-title">Please enter a valid URL for which screenshot needs to be
                                    captured.</h3>
                            </div>
                            <!-- /.card-header -->
                            <!-- form start -->
                            <form role="form" id="quickForm">
                                <div class="card-body">
                                    <div class="form-group">
                                        <label for="url">URL</label>
                                        <input type="url" name="url" class="form-control" id="url" placeholder="Ex: https://www.google.com">
                                    </div>
                                </div>
                                <!-- /.card-body -->
                                <div class="card-footer">
                                    <button type="submit" class="btn btn-primary">Submit</button>
                                </div>
                            </form>
                        </div>
                        <!-- /.card -->
                        <!-- /.card -->
                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
            </div>
            <!-- /.container-fluid -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <%@ include file="../common/footer.jspf" %>
</div>
<!-- ./wrapper -->
<script type="text/javascript">
    $.validator.setDefaults({
        submitHandler: function () {
            var url = $("#url").val()
            $(".card").append(showOverLay("Capturing the screenshot of " + url));
            $.when(getScreenShot(url)).done(function (data) {
                if (data.status == "success") {
                    alert("success. check the  "+data.filename+".png in c:/tmp folder for the captured file.")

                } else {

                }
                $(".card").find("#processing-overlay").remove();
            })

        }
    });

    function showOverLay(message){
        var html = ''
        html +='<div id="processing-overlay" class="overlay"><i class="fas fa-2x fa-sync-alt fa-spin"></i>'+"&nbsp;"+message+'</div>'
        return html
    }

    function getScreenShot(url) {
        return $.ajax({
            type: 'GET',
            url: "/getScreenShot",
            data: {url: url},
        });
    }

    $('#quickForm').validate({
        rules: {
            url: {
                required: true
            },
        },
        messages: {
            url: "URL cannot be blank"
        },
        errorElement: 'span',
        errorPlacement: function (error, element) {
            error.addClass('invalid-feedback');
            element.closest('.form-group').append(error);
        },
        highlight: function (element, errorClass, validClass) {
            $(element).addClass('is-invalid');
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).removeClass('is-invalid');
        }
    });

</script>
