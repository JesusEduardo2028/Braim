module Songbook
  module Entities
    class User < Grape::Entity
      expose :id, documentation: { type: "string", desc: "id of the entry", example: '172a66834fb7802c28000003' } do |band, options|
        band.id.to_s
      end
      expose :username, documentation: { type: "string", desc: "username", example: "chuchobass" }
      expose :email, documentation: { type: "string", desc: "email", example: "chucho@example.com" }
    end
  end
end
