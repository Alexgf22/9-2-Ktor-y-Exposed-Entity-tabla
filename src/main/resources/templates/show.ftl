<#-- @ftlvariable name="entity" type="com.example.models.Entity" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>
            ${entity.name}
        </h3>
        <p>
            ${entity.description}
        </p>
        <p>
            ${entity.value}
        </p>
        <p>
            ${entity.sectionId}
        </p>
        <p>
            ${entity.order}
        </p>
        <hr>
        <p>
            <!-- Este enlace en la parte inferior de esta página debe abrir un formulario
             para editar o eliminar este item. -->
            <a href="/entities/${entity.id}/edit">Edit entity</a>
        </p>
    </div>
</@layout.header>