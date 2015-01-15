module Songbook
  module Modules
    class User < Grape::API
      include Grape::Transformations::Base

      target_model ::User

      format :json

      content_type :json, 'application/json'

      helpers do
        # Pagination stuff, unfortunately it cannot be located in API root class
        params :pagination do
          optional :page, type: Integer
          optional :per_page, type: Integer
        end

        params :auth do
          requires :songbook_token, type: String, desc: 'Auth token', documentation: { example: '837f6b854fc7802c2800302e' }
        end

        params :id do
          requires :id, type: String, desc: 'User ID', regexp: /^[[:xdigit:]]{24}$/
        end

      end

      before_validation do
        authenticated_user?
      end

      define_endpoints do |entity|
        desc 'returns all existent users', {
          entity: entity,
          notes: <<-NOTES
            ##Description
          NOTES
        }
        params do
          use :auth
        end
        get '/', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
          content_type "text/json"
          page = params[:page] || 1
          per_page = params[:per_page] || 10
          WillPaginate.per_page = per_page
          users = ::User.page(page)
          present users, with: entity
        end
        desc 'returns all existent users', {
          entity: entity,
          notes: <<-NOTES
            ##Description
          NOTES
        }
        params do
          use :auth
        end
        get '/:id', http_codes: [ [200, "Successful"], [401, "Unauthorized"] ] do
          content_type "text/json"
          user = ::User.find(params[:id])
          present user, with: entity
        end
      end

      version :v1 do
        resource :users do
          add_endpoints
        end
      end
    end
  end
end
