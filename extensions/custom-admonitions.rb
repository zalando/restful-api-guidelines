# Registers EXCEPTION and EXAMPLE as custom admonition block types.
# Reference: https://github.com/asciidoctor/asciidoctor-extensions-lab/issues/9
Asciidoctor::Extensions.register do
  block do
    named :EXCEPTION
    on_context :example
    process do |parent, reader, attrs|
      attrs['name'] = 'exception'
      attrs['caption'] = 'Exception'
      create_block parent, :admonition, reader.read_lines, attrs, content_model: :compound
    end
  end
  block do
    named :EXAMPLE
    on_context :example
    process do |parent, reader, attrs|
      attrs['name'] = 'example'
      attrs['caption'] = 'Example'
      create_block parent, :admonition, reader.read_lines, attrs, content_model: :compound
    end
  end
end
