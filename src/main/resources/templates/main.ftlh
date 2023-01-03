<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="mb-3">
        <form method="get" action="/main" class="row g-2 col-lg-6">
            <div class="col-lg-4">
                <input class="form-control" type="text" name="filter" value="${filter?ifExists}"
                       placeholder="Search by tag"/>
            </div>
            <div class="col-lg-auto">
                <button class="btn btn-primary ms-6" type="submit">Search</button>
            </div>
        </form>
    </div>

    <#include "parts/messageEdit.ftlh" />

    <#include "parts/messageList.ftlh" />

</@c.page>