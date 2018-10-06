import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './area.reducer';
import { IArea } from 'app/shared/model/area.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

import { getEntitiesByArea } from '../customer/customer.reducer';

export interface IAreaDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AreaDetail extends React.Component<IAreaDetailProps> {
  componentDidMount() {
    const id = this.props.match.params.id;
    this.props.getEntity(id);
    this.props.getEntitiesByArea(parseInt(id, 10));
  }

  render() {
    const { areaEntity, customerList } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="abstraktApp.area.detail.title">Area</Translate> [<b>{areaEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="abstraktApp.area.name">Name</Translate>
              </span>
            </dt>
            <dd>{areaEntity.name}</dd>
          </dl>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="abstraktApp.area.customers">Customers</Translate>
              </span>
            </dt>
            <div className="table-responsive">
              <Table responsive>
                <thead>
                  <tr>
                    <th>
                      <Translate contentKey="global.field.id">ID</Translate>
                    </th>
                    <th>
                      <Translate contentKey="abstraktApp.customer.name">Name</Translate>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {customerList.map((customer, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`/entity/customer/${customer.id}`} color="link" size="sm">
                          {customer.id}
                        </Button>
                      </td>
                      <td>{customer.name}</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          </dl>
          <Button tag={Link} to="/entity/area" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/area/${areaEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ area, customer }: IRootState) => ({
  areaEntity: area.entity,
  customerList: customer.entities
});

const mapDispatchToProps = { getEntity, getEntitiesByArea };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AreaDetail);
