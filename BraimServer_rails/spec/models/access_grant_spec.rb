require 'spec_helper'

describe AccessGrant, :type => :model do

  context 'when is created a single AccessGrant instance' do

    let(:access_grant) { FactoryGirl.create(:access_grant) }

    it 'verifies if access token have been created' do
      expect(access_grant.access_token).to match(/^\h{32}$/)
    end

  end

  context 'when already exists many AccessGrant instances created' do

    before :each do
      FactoryGirl.create_list(:access_grant, 3)
    end

    context 'when all access_grant instances are expired' do

      before :each do
        AccessGrant.all.map {|access_grant| access_grant.update_attributes(access_token_expires_at: 5.hours.ago, created_at: 8.hours.ago)}
      end

      it 'finds access_grant instance by access_token' do
        existent_token = AccessGrant.first.access_token
        expect(AccessGrant.find_access(existent_token)).to be_nil
      end

      it 'removes all expired access_grant instances' do
        AccessGrant.prune!
        expect(AccessGrant.count).to be 0
      end
    end

    context 'when one or more access_grant instances are valid' do

      it 'finds access_grant instance by access_token' do
        existent_token = AccessGrant.first.access_token
        expect(AccessGrant.find_access(existent_token)).to be_a_kind_of AccessGrant
      end

    end

  end
end
