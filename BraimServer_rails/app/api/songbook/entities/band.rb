module Songbook
  module Entities
    class Band < Grape::Entity
      expose :id, documentation: { type: "string", desc: "id of the entry", example: '172a66834fb7802c28000003' } do |band, options|
        band.id.to_s
      end
      expose :name, documentation: { type: "string", desc: "band name", example: "Superlitio" }
      expose :biography, documentation: { type: "string", desc: "band biography", example: "short bio" }
      expose :genre, documentation: { type: "string", desc: "band genre", example: "Rock" }
    end
  end
end
