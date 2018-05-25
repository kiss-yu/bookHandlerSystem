function getQueryString() {
    var qs = location.search.substr(1), // 获取url中"?"符后的字串
        args = {}, // 保存参数数据的对象
        items = qs.length ? qs.split("&") : [], // 取得每一个参数项,
        item = null,
        len = items.length;

    for(var i = 0; i < len; i++) {
        item = items[i].split("=");
        var name = decodeURIComponent(item[0]),
            value = decodeURIComponent(item[1]);
        if(name) {
            args[name] = value;
        }
    }
    return args;
}
function getMemberRoleName() {
    return JSON.parse(localStorage.getItem("member")).role.value;
}
var id = getQueryString()["id"];

var listTable;
var apiRoot;
$(function () {
    listTable = $("#listTable");

    //1.初始化Table
    var oTable;
    try {
        oTable = new TableInit();
    }catch (e) {
        return;
    }
    oTable.Init();

    $("#btn_add").click(function () {
        location.href = apiRoot + "/add.html";
    });
    $("#btn_delete").click(function () {
        var tables = listTable.bootstrapTable('getSelections');
        console.log(tables[0])
        var lines = new Array();
        for (var i = 0; i < tables.length; i++) {
            lines.push(tables[i].id);
        }
        if(confirm('确认删除所有选中数据?') == true){
            $.ajax({
                method:'POST',
                url: apiRoot + '/delete',
                data:{ids:lines},
                traditional:true,
                success : function(o) {
                    if(o.status == 1){
                        $("#btn_delete,#btn_edit").hide();
                        listTable.bootstrapTable('remove', {field: 'id', values: lines});
                    }else {
                        alert(o.msg == null ? "删除失败" : o.msg);
                    }
                }
            });
        }
    })
});

//得到查询的参数
var queryParams = function (params) {
    console.log(params);
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
        limit: params.limit,   //页面大小
        page: params.offset / params.limit + 1,  //页码
    };
    return temp;
};
function deleteRole(id) {
    $.ajax({
        method:'POST',
        url: apiRoot + '/delete',
        data:{ids:[id]},
        traditional:true,
        success : function(o) {
            if(o.status == 1){
                listTable.bootstrapTable('remove', {field: 'id', values: [id]});
            }else {
                alert(o.msg == null ? "删除失败" : o.msg);
            }
        }
    });
}
function deleteModel(id) {
    $.ajax({
        method:'POST',
        url: apiRoot + '/delete',
        data:{ids:[id]},
        traditional:true,
        success : function(o) {
            if(o.status == 1){
                listTable.bootstrapTable('remove', {field: 'id', values: [id]});
            }else {
                alert(o.msg == null ? "删除失败" : o.msg);
            }
        }
    });
}(function($) {

    var zIndex = 100;

    // 消息框
    var $message;
    var messageTimer;
    $.message = function() {
        var message = {};
        if ($.isPlainObject(arguments[0])) {
            message = arguments[0];
        } else if (typeof arguments[0] === "string" && typeof arguments[1] === "string") {
            message.type = arguments[0];
            message.content = arguments[1];
        } else {
            return false;
        }

        if (message.type == null || message.content == null) {
            return false;
        }

        if ($message == null) {
            $message = $('<div class="xxMessage"><div class="messageContent message' + escapeHtml(message.type) + 'Icon"><\/div><\/div>');
            if (!window.XMLHttpRequest) {
                $message.append('<iframe class="messageIframe"><\/iframe>');
            }
            $message.appendTo("body");
        }

        $message.children("div").removeClass("messagewarnIcon messageerrorIcon messagesuccessIcon").addClass("message" + message.type + "Icon").html(message.content);
        $message.css({"margin-left": - parseInt($message.outerWidth() / 2), "z-index": zIndex ++}).show();

        clearTimeout(messageTimer);
        messageTimer = setTimeout(function() {
            $message.hide();
        }, 3000);
        return $message;
    };

    // 对话框
    $.dialog = function(options) {
        var settings = {
            width: 320,
            height: "auto",
            modal: true,
            ok: '确 定',
            cancel: '取 消',
            onShow: null,
            onClose: null,
            onOk: null,
            onCancel: null
        };
        $.extend(settings, options);

        if (settings.content == null) {
            return false;
        }

        var $dialog = $('<div class="xxDialog"><\/div>');
        var $dialogTitle;
        var $dialogClose = $('<div class="dialogClose"><\/div>').appendTo($dialog);
        var $dialogContent;
        var $dialogBottom;
        var $dialogOk;
        var $dialogCancel;
        var $dialogOverlay;
        if (settings.title != null) {
            $dialogTitle = $('<div class="dialogTitle"><\/div>').appendTo($dialog);
        }
        if (settings.type != null) {
            $dialogContent = $('<div class="dialogContent dialog' + escapeHtml(settings.type) + 'Icon"><\/div>').appendTo($dialog);
        } else {
            $dialogContent = $('<div class="dialogContent"><\/div>').appendTo($dialog);
        }
        if (settings.ok != null || settings.cancel != null) {
            $dialogBottom = $('<div class="dialogBottom"><\/div>').appendTo($dialog);
        }
        if (settings.ok != null) {
            $dialogOk = $('<input type="button" class="button" value="' + escapeHtml(settings.ok) + '" \/>').appendTo($dialogBottom);
        }
        if (settings.cancel != null) {
            $dialogCancel = $('<input type="button" class="button" value="' + escapeHtml(settings.cancel) + '" \/>').appendTo($dialogBottom);
        }
        if (!window.XMLHttpRequest) {
            $dialog.append('<iframe class="dialogIframe"><\/iframe>');
        }
        $dialog.appendTo("body");
        if (settings.modal) {
            $dialogOverlay = $('<div class="dialogOverlay"><\/div>').insertAfter($dialog);
        }

        var dragStart = {};
        var dragging = false;
        if (settings.title != null) {
            $dialogTitle.text(settings.title);
        }
        $dialogContent.html(settings.content);
        $dialog.css({"width": settings.width, "height": settings.height, "margin-left": - parseInt(settings.width / 2), "z-index": zIndex ++});
        dialogShow();

        if ($dialogTitle != null) {
            $dialogTitle.mousedown(function(event) {
                $dialog.css({"z-index": zIndex ++});
                var offset = $dialog.offset();
                dragStart.pageX = event.pageX;
                dragStart.pageY = event.pageY;
                dragStart.left = offset.left;
                dragStart.top = offset.top;
                dragging = true;
                return false;
            }).mouseup(function() {
                dragging = false;
            });

            $(document).mousemove(function(event) {
                if (dragging) {
                    $dialog.offset({"left": dragStart.left + event.pageX - dragStart.pageX, "top": dragStart.top + event.pageY - dragStart.pageY});
                    return false;
                }
            }).mouseup(function() {
                dragging = false;
            });
        }

        if ($dialogClose != null) {
            $dialogClose.click(function() {
                dialogClose();
                return false;
            });
        }

        if ($dialogOk != null) {
            $dialogOk.click(function() {
                if (settings.onOk && typeof settings.onOk == "function") {
                    if (settings.onOk($dialog) != false) {
                        dialogClose();
                    }
                } else {
                    dialogClose();
                }
                return false;
            });
        }

        if ($dialogCancel != null) {
            $dialogCancel.click(function() {
                if (settings.onCancel && typeof settings.onCancel == "function") {
                    if (settings.onCancel($dialog) != false) {
                        dialogClose();
                    }
                } else {
                    dialogClose();
                }
                return false;
            });
        }

        function dialogShow() {
            if (settings.onShow && typeof settings.onShow == "function") {
                if (settings.onShow($dialog) != false) {
                    $dialog.show();
                    $dialogOverlay.show();
                }
            } else {
                $dialog.show();
                $dialogOverlay.show();
            }
        }

        function dialogClose() {
            if (settings.onClose && typeof settings.onClose == "function") {
                if (settings.onClose($dialog) != false) {
                    $dialogOverlay.remove();
                    $dialog.remove();
                }
            } else {
                $dialogOverlay.remove();
                $dialog.remove();
            }
        }
        return $dialog;
    };

    $.fn.extend({
        // 文件上传
        uploader: function(options) {
            var settings = {
                url: '/admin/file/upload.jhtml',
                fileType: "image",
                fileName: "file",
                data: {},
                maxSize: 10,
                extensions: null,
                before: null,
                complete: null
            };
            $.extend(settings, options);

            if (settings.extensions == null) {
                switch(settings.fileType) {
                    case "media":
                        settings.extensions = 'swf,flv,mp3,wav,avi,rm,rmvb';
                        break;
                    case "file":
                        settings.extensions = 'zip,rar,7z,doc,docx,xls,xlsx,ppt,pptx';
                        break;
                    default:
                        settings.extensions = 'jpg,jpeg,bmp,gif,png';
                }
            }

            var $progressBar = $('<div class="progressBar"><\/div>').appendTo("body");
            return this.each(function() {
                var element = this;
                var $element = $(element);

                var webUploader = WebUploader.create({
                    swf: '/resources/admin/flash/webuploader.swf',
                    server: settings.url + (settings.url.indexOf('?') < 0 ? '?' : '&') + 'fileType=' + settings.fileType + '&token=' + getCookie("token"),
                    pick: {
                        id: element,
                        multiple: false
                    },
                    fileVal: settings.fileName,
                    formData: settings.data,
                    fileSingleSizeLimit: settings.maxSize * 1024 * 1024,
                    accept: {
                        extensions: settings.extensions
                    },
                    fileNumLimit: 1,
                    auto: true
                }).on('beforeFileQueued', function(file) {
                    if ($.isFunction(settings.before) && settings.before.call(element, file) === false) {
                        return false;
                    }
                    if ($.trim(settings.extensions) == '') {
                        this.trigger('error', 'Q_TYPE_DENIED');
                        return false;
                    }
                    this.reset();
                    $progressBar.show();
                }).on('uploadProgress', function(file, percentage) {
                    $progressBar.width(percentage * 100 + '%');
                }).on('uploadAccept', function(file, data) {
                    $progressBar.fadeOut("slow", function() {
                        $progressBar.width(0);
                    });
                    if (data.message.type != 'success') {
                        $.message(data.message);
                        return false;
                    }
                    $element.prev("input:text").val(data.url);
                    if ($.isFunction(settings.complete)) {
                        settings.complete.call(element, file, data);
                    }
                }).on('error', function(type) {
                    switch(type) {
                        case "F_EXCEED_SIZE":
                            $.message("warn", "上传文件大小超出限制");
                            break;
                        case "Q_TYPE_DENIED":
                            $.message("warn", "上传文件格式不正确");
                            break;
                        default:
                            $.message("warn", "上传文件出现错误");
                    }
                });

                $element.mouseover(function() {
                    webUploader.refresh();
                });
            });
        },

        // 编辑器
        editor: function(options) {
            window.UEDITOR_CONFIG = {
                UEDITOR_HOME_URL: '/resource/ueditor/',
                serverUrl: '/system/upload',
                imageActionName: "uploadImage",
                imageFieldName: "file",
                imageMaxSize: 10485760,
                imageAllowFiles: ['.jpg', '.jpeg', '.bmp', '.gif', '.png'],
                imageUrlPrefix: "",
                imagePathFormat: "",
                imageCompressEnable: false,
                imageCompressBorder: 1600,
                imageInsertAlign: "none",
                videoActionName: "uploadMedia",
                videoFieldName: "file",
                videoMaxSize: 10485760,
                videoAllowFiles: ['.swf', '.flv', '.mp3', '.wav', '.avi', '.rm', '.rmvb'],
                videoUrlPrefix: "",
                videoPathFormat: "",
                fileActionName: "uploadFile",
                fileFieldName: "file",
                fileMaxSize: 10485760,
                fileAllowFiles: ['.zip', '.rar', '.7z', '.doc', '.docx', '.xls', '.xlsx', '.ppt', '.pptx'],
                fileUrlPrefix: "",
                filePathFormat: "",
                toolbars: [[
                    'fullscreen', 'source', '|',
                    'undo', 'redo', '|',
                    'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|',
                    'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
                    'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
                    'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
                    'directionalityltr', 'directionalityrtl', 'indent', '|',
                    'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
                    'touppercase', 'tolowercase', '|',
                    'link', 'unlink', 'anchor', '|',
                    'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
                    'insertimage', 'insertvideo', 'attachment', 'map', 'insertframe', 'pagebreak', '|',
                    'horizontal', 'date', 'time', 'spechars', '|',
                    'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', '|',
                    'print', 'preview', 'searchreplace', 'drafts'
                ]],
                lang: 'zh_CN',
                iframeCssUrl: null,
                pageBreakTag: 'jfinalshop_page_break_tag',
                wordCount: false
            };

            UE.Editor.prototype.getActionUrl = function(action) {
                var serverUrl = this.getOpt('serverUrl');
                switch(action) {
                    case "uploadImage":
                        return serverUrl + (serverUrl.indexOf('?') < 0 ? '?' : '&') + 'fileType=image';
                    case "uploadMedia":
                        return serverUrl + (serverUrl.indexOf('?') < 0 ? '?' : '&') + 'fileType=media';
                    case "uploadFile":
                        return serverUrl + (serverUrl.indexOf('?') < 0 ? '?' : '&') + 'fileType=file';
                }
                return null;
            };

            UE.Editor.prototype.loadServerConfig = function() {
                this._serverConfigLoaded = true;
            };

            return this.each(function() {
                var element = this;
                var $element = $(element);

                UE.getEditor($element.attr("id"), options).ready(function() {
                });
            });
        }

    });

})(jQuery);

// Html转义
function escapeHtml(str) {
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
// 字符串缩略
function abbreviate(str, width, ellipsis) {
    if ($.trim(str) == "" || width == null) {
        return str;
    }
    var i = 0;
    for (var strWidth = 0; i < str.length; i++) {
        strWidth = /^[\u4e00-\u9fa5\ufe30-\uffa0]$/.test(str.charAt(i)) ? strWidth + 2 : strWidth + 1;
        if (strWidth >= width) {
            break;
        }
    }
    return ellipsis != null && i < str.length - 1 ? str.substring(0, i) + ellipsis : str.substring(0, i);
}

function clickImage(file) {
    if (file.files && file.files[0]) {
        var reader = new FileReader();
        reader.onload = function(evt) {
            console.log(evt)
            document.getElementById("portrait").src = evt.target.result;
        }
        reader.readAsDataURL(file.files[0]);
    }
}