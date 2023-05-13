<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Create entity</h3>
        <form action="/entities" method="post">
            <p>
                <label>
                    <input type="text" name="value">
                </label>
            </p>

            <p>
                <label>
                    <input type="text" name="name">
                </label>
            </p>

            <p>
                <label>
                    <textarea name="description"></textarea>
                </label>
            </p>

            <p>
                <label>
                    <input type="text" name="sectionId">
                </label>
            </p>

            <p>
                <label>
                    <input type="number" name="order">
                </label>
            </p>

            <p>
                <input type="submit">
            </p>
        </form>
    </div>
</@layout.header>