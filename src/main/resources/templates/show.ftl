<#-- @ftlvariable name="entity" type="com.example.models.Entity" -->
<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>
           Name: ${entity.name}
        </h3>
        <p>
           Description: ${entity.description}
        </p>
        <p>
           Value: ${entity.value}
        </p>
        <p>
           SectionId: ${entity.sectionId}
        </p>
        <p>
            Order: ${entity.order}
        </p>
        <hr>
        <p>
            <!-- Este enlace en la parte inferior de esta página debe abrir un formulario
             para editar o eliminar este item. -->
            <a href="/entities/${entity.id}/edit">Edit entity</a>
        </p>
    </div>
</@layout.header>