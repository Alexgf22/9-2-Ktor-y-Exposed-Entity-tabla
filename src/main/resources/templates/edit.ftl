<#-- @ftlvariable name="entity" type="com.example.models.Entity" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Edit entity</h3>
        <form action="/entities/${entity.id}" method="post">
            <p>
                <label>
                    <input type="text" name="value" value="${entity.value}">
                </label>
            </p>

            <p>
                <label>
                    <input type="text" name="name" value="${entity.name}">
                </label>
            </p>

            <p>
                <label>
                    <textarea name="description">${entity.description}</textarea>
                </label>
            </p>

            <p>
                <label>
                    <input type="text" name="sectionId" value="${entity.sectionId}">
                </label>
            </p>

            <p>
                <label>
                    <input type="number" name="order" value="${entity.order}">
                </label>
            </p>

            <p>
                <input type="submit" name="_action" value="update">
            </p>
        </form>
    </div>
    <div>
        <form action="/entities/${entity.id}" method="post">
            <p>
                <input type="submit" name="_action" value="delete">
            </p>
        </form>
    </div>
</@layout.header>