<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="mb-3">
        <form method="get" action="/main" class="row g-2 col-lg-6">
            <div class="col-lg-4">
                <input class="form-control" type="text" name="filter" value="${filter?ifExists}"
                       placeholder="Search by tag">
            </div>
            <div class="col-lg-auto">
                <button class="btn btn-primary ms-6" type="submit">Search</button>
            </div>
        </form>
    </div>


    <div class="mb-3">
        <a class="btn btn-primary" data-bs-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
           aria-controls="collapseExample">
            Add new message
        </a>
    </div>

    <div class="collapse <#if message??>show</#if> mb-3" id="collapseExample">
        <form method="post" enctype="multipart/form-data">
            <div class="input-group mb-3">
                <input type="text" class="form-control ${(textError??)?string('is-invalid', "")}"
                       value="<#if message??>${message.text}</#if>" name="text" placeholder="Введите сообщение"/>
                <#if textError??>
                <div class="invalid-feedback">
                    ${textError}
                </div>
                </#if>
            </div>
            <div class="input-group mb-3">
                <input type="text" class="form-control ${(tagError??)?string('is-invalid', "")}"
                       value="<#if message??>${message.tag}</#if>" name="tag" placeholder="Тэг"/>
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>
            <div class="input-group mb-3">
                <input type="file" class="form-control" id="customFile" name="file">
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="input-group mb-3">
                <button type="submit" class="btn btn-primary">Добавить</button>
            </div>
        </form>
    </div>
    <div class="row row-cols-1 row-cols-md-3 g-4">
        <#list messages as message>
            <div class="col">
                <div class="card h-100">
                    <#if message.filename??>
                        <img src="/img/${message.filename}" class="card-img-top">
                    </#if>
                    <div>
                        <span>${message.text}</span>
                        <i>${message.tag}</i>
                    </div>
                    <div class="card-footer text-muted">
                        ${message.authorName}
                    </div>
                </div>
            </div>
        <#else>
            No message
        </#list>
    </div>
</@c.page>