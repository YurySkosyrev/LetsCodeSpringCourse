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

    <div class="collapse mb-3" id="collapseExample">
        <div class="form-group mt-2">
            <form method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <input type="text" class="form-control" name="text" placeholder="Введите сообщение"/>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="tag" placeholder="Тэг"/>
                </div>
                <div class="form-group">
                    <div class="custom-file">
                        <input type="file" name="file" id="customFile">
                        <label class="custom-file-label" for="customFile">Choosen file</label>
                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Добавить</button>
                </div>
            </form>
        </div>
    </div>

    <#list messages as message>
        <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
            <div>
                <#if message.filename??>
                    <img src="/img/${message.filename}">
                </#if>
            </div>
        </div>
    <#else>
        No message
    </#list>
</@c.page>