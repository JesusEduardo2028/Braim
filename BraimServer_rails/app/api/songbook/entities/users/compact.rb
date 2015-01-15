module Songbook
  module Entities
    module Users
      class Compact < Grape::Entity
        expose :email, documentation: { type: "email", desc: "user email", example: "allam_britto@example.com" }
      end
    end
  end
end
