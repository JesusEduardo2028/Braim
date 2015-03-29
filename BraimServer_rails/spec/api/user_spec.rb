describe 'Band', :type => :request do

  describe :v1 do

    context 'bands', :keep_db do

      before :context do
        @user = FactoryGirl.create :user, email: 'allam.britto@fake.com', password: '12345678', password_confirmation: '12345678'
        FactoryGirl.create_list(:user, 20)
        get '/api/v1/token', %Q{data=allam.britto@fake.com&password=12345678}
        @credentials = JSON.parse response.body
      end

      context 'GET' do

        it 'verifies that response has the elements number specified in per_page param' do
          token = "braim_token=#{@credentials['access_token']}"
          per_page = 5
          data = "per_page=#{per_page}"

          get '/api/v1/users', "#{token}&#{data}"

          expect(response.status).to eq 200
          expect(JSON.parse(response.body).count).to be per_page
        end

        context '/:username' do

          it 'gets user by username' do

            user = User.last

            expected_response = {
              id: user.id.to_s,
              username: user.username,
              email: user.email
            }

            get "/api/v1/users/#{user.username}", braim_token: @credentials['access_token']
            expect(response.status).to eq 200
            expect(JSON.parse(response.body)).to match expected_response.stringify_keys
          end

        end

      end

    end

  end

end
