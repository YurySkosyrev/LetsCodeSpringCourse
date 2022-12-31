<div class="mb-3">
    <a class="btn btn-primary" data-bs-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        Message Edit
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
        <input type="hidden" name="id" value="<#if message??>${message.id}</#if>"/>
        <div class="input-group mb-3">
            <button type="submit" class="btn btn-primary">Save Message</button>
        </div>
    </form>
</div>